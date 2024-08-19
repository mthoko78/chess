import React, { useState } from "react";

// @mui material components
import Grid from "@mui/material/Grid";

// Material Kit 2 React components
import Tab from "@mui/material/Tab";
import Tabs from "@mui/material/Tabs";
import AppBar from "@mui/material/AppBar";

const GameOptions = (props) => {

  const resign = `Resign`;
  const offerDraw = `Offer draw`;
  const restart = `Restart game`;

  const performAction = (action) => {
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
    // setActiveTab(newValue);
  };

  const [time, setTime] = useState(". . .");

  function timeElapsed() {
    // eslint-disable-next-line react/prop-types
    let gameDate = new Date(props.gameDate);
    let date = new Date();
    let totalSeconds = (9 * 60) - Math.round((date.valueOf() - gameDate.valueOf()) / (1000));
    const minutes = Math.round(totalSeconds / (60));
    const seconds = totalSeconds % 60;
    return (minutes < 10 ? "0" + minutes : minutes) + ":" + (seconds < 10 ? "0" + seconds : seconds);
  }

  function incrementSeconds() {
    let timeElapsed1 = timeElapsed();
    setTime(timeElapsed1);
    const minute = parseInt(timeElapsed1.substring(0, 2));
    const second = parseInt(timeElapsed1.substring(3, 5));
    if (minute <= 0 && second <= 0) {
      performAction(resign);
    }
  }

  setTimeout(() => incrementSeconds(), 1000);

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
};

export default GameOptions;