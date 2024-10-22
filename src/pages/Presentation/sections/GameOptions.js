import React, { forwardRef, useEffect, useImperativeHandle, useState } from "react";

// @mui material components
import Grid from "@mui/material/Grid";

// Material Kit 2 React components
import Tab from "@mui/material/Tab";
import Tabs from "@mui/material/Tabs";
import AppBar from "@mui/material/AppBar";

const TIME_TRACKER = "timeTracker";



const dateDiffInMillis = (dateFrom, dateTo) => {
  return Math.abs(dateTo.valueOf() - dateFrom.valueOf());
};

export const calculateTimeSpent = (game, username) => {
  let whiteTime = 0;
  let blackTime = 0;
  let current = new Date(game.created);

  if (game.moves) {
    for (let i = 0; i < game.moves.length; i++) {
      const diff = dateDiffInMillis(current.valueOf(), new Date(game.moves[i].timestamp));
      if (i % 2 === 0) { //even number for white
        whiteTime += diff;
      } else {
        blackTime += diff;
      }
      current = new Date(game.moves[i].timestamp);
    }
    let time = game.whitePlayer.username === username ? whiteTime : blackTime;
    if (game.currentPlayer === username) {
      time += dateDiffInMillis(current, new Date());
    }
    console.log(blackTime, whiteTime);
    return Math.floor(time / 1000);
  } else {
    //no moves yet so white is to move and elapsed time counts for them
    return game.whitePlayer.username === username ? Math.floor(dateDiffInMillis(new Date(), new Date(game.created)) / 1000) : 0;
  }
};

const GameOptions = forwardRef((props, optionsRef) => {
  const resign = `Resign`;
  const offerDraw = `Offer draw`;
  const restart = `Restart game`;
  const [timeLeft, setTimeLeft] = useState(10 * 60);

  const performAction = (action) => {
    console.log("Performing");
    switch (action) {
      case resign: {
        // eslint-disable-next-line react/prop-types
        props.resign();
        break;
      }
      case offerDraw: {
        // eslint-disable-next-line react/prop-types
        props.offerDraw();
        break;
      }
      case restart: {
        // eslint-disable-next-line react/prop-types
        props.restart();
        break;
      }
    }
  };

  const [activeTab] = useState(1);

  const handleTabType = () => {
  };

  const [time, setTime] = useState();

  function secondsToTime(totalSeconds) {
    const minutes = Math.floor(totalSeconds / (60));
    const seconds = totalSeconds % 60;
    console.log("total seconds", totalSeconds);
    console.log("mins", minutes);
    console.log("secs", seconds);
    return (minutes < 10 ? "0" + minutes : minutes) + ":" + (seconds < 10 ? "0" + seconds : seconds);
  }

  useImperativeHandle(optionsRef, () => ({
    countDown() {
      console.log("Time tracker BEFORE update:", timeTracker);
      let updatedTracker;
      if (!timeTracker.timeUpToDate) {
        const lostTime = Math.round((new Date().valueOf() - new Date(timeTracker.timestamp).valueOf()) / 1000);
        console.log("lost time:", lostTime);
        updatedTracker = {
          ...timeTracker,
          timeUpToDate: true,
          secondsLeft: timeTracker.secondsLeft - lostTime - 1,
          timestamp: new Date()
        };
        //   TODO: Check is player's time is up and take appropriate action(s)
      } else {
        updatedTracker = {
          ...timeTracker,
          timeUpToDate: true,
          secondsLeft: timeTracker.secondsLeft - 1,
          timestamp: new Date()
        };
      }
      setTimeTracker(updatedTracker);
      localStorage.setItem(TIME_TRACKER, JSON.stringify(updatedTracker));
      console.log("Time tracker AFTER update:", updatedTracker);

      setTimeLeft(timeLeft - 1);
      setTime(secondsToTime(timeLeft));
      if (timeLeft <= 0) {
        performAction(restart);
      }
    }
  }));

  const [timeTracker, setTimeTracker] = useState({ timestamp: new Date(), secondsLeft: 10 * 60, timeUpToDate: false });

  useEffect(() => {
    console.log("Hello")
  }, []);

  return (
    <Grid container item justifyContent="center" xs={12} md={10} lg={10} xl={10} mx="auto" py={2} px={1}>
      <AppBar position="static">
        <Tabs value={activeTab} onChange={handleTabType}>
          <Tab style={{ fontSize: 22, background: "lightsalmon" }} label={resign}
               onClick={() => performAction(resign)} />
          <Tab style={{ fontSize: 28, fontWeight: "bold", background: "white" }} label={time}
               onClick={(e) => e.preventDefault()} />
          <Tab style={{ fontSize: 22, background: "lightskyblue" }} label={offerDraw}
               onClick={() => performAction(offerDraw)} />
        </Tabs>
      </AppBar>
    </Grid>
  );
});

export default GameOptions;