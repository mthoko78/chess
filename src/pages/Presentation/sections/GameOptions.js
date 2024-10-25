import React, { forwardRef, useEffect, useImperativeHandle, useState } from "react";

// @mui material components
import Grid from "@mui/material/Grid";

// Material Kit 2 React components
import Tab from "@mui/material/Tab";
import Tabs from "@mui/material/Tabs";
import AppBar from "@mui/material/AppBar";

export const GAME_DURATION = 30*60 // 30 minutes

export const dateDiffInMillis = (dateFrom, dateTo) => {
  return Math.abs(dateTo.valueOf() - dateFrom.valueOf());
};

export const secondsToTime = (totalSeconds) => {
 const minutes = Math.floor(totalSeconds / (60));
 const seconds = totalSeconds % 60;
 return (minutes < 10 ? "0" + minutes : minutes) + ":" + (seconds < 10 ? "0" + seconds : seconds);
}

export const calculateTimeSpent = (game, username) => {
  let whiteTime = 0;
  let blackTime = 0;
  let current = new Date(game.created);

  if (game.moves && (game.moves.length > 0)) {
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
  // eslint-disable-next-line react/prop-types
  const timeSpent = calculateTimeSpent(props.game, localStorage.getItem("username"))
  // eslint-disable-next-line react/prop-types
  const [timeLeft, setTimeLeft] = useState(GAME_DURATION - timeSpent);

  const [time, setTime] = useState(secondsToTime(GAME_DURATION - timeSpent));

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

  useImperativeHandle(optionsRef, () => ({
    countDown() {
      setTimeLeft(timeLeft - 1);
      setTime(secondsToTime(timeLeft));
      if (timeLeft <= 0) {
        performAction(restart);
      }
    }
  }));

  useEffect(() => {

    // eslint-disable-next-line react/prop-types
    const timeSpent = calculateTimeSpent(props.game, localStorage.getItem("username"))
    console.log("time spent", timeSpent);

    const timeLeft = GAME_DURATION - timeSpent
    console.log("your time left", timeLeft);
    // eslint-disable-next-line react/prop-types
    const seconds = Math.round(dateDiffInMillis(new Date(props.game.created), new Date())/1000)
    console.log("game duration", seconds);
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