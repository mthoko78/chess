import React, { useState } from "react";

// @mui material components
import Grid from "@mui/material/Grid";

// Material Kit 2 React components
import Tab from "@mui/material/Tab";
import Tabs from "@mui/material/Tabs";
import AppBar from "@mui/material/AppBar";

const OpponentInfo = (props) => {

  const [activeTab] = useState(1);

  const [time, setTime] = useState(". . .");

  function timeElapsed() {
    // eslint-disable-next-line react/prop-types
    let gameDate = new Date(props.gameDate);
    let date = new Date();
    let totalSeconds = (120) - Math.round((date.valueOf() - gameDate.valueOf()) / (1000));
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
      console.log("Game ended");
    }
  }

  setTimeout(() => incrementSeconds(), 1000);

  return (
    <Grid container item justifyContent="center" xs={12} md={10} lg={10} xl={10} mx="auto" py={2} px={1}>
      <AppBar position="static" style={{backgroundColor:"gray !important"}}>
        <Tabs value={activeTab}>
          {/* eslint-disable-next-line react/prop-types */}
          <Tab style={{ fontSize: 22, background: "antiquewhite" }} label={props.opponentName} />
          <Tab style={{ fontSize: 28, fontWeight: "bold", background: "white" }} label={time} />
        </Tabs>
      </AppBar>
    </Grid>
  );
};

export default OpponentInfo;