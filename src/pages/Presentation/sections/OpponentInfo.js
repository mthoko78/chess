import React, { forwardRef, useImperativeHandle, useState } from "react";

// @mui material components
import Grid from "@mui/material/Grid";

// Material Kit 2 React components
import Tab from "@mui/material/Tab";
import Tabs from "@mui/material/Tabs";
import AppBar from "@mui/material/AppBar";

const OpponentInfo = forwardRef((props, ref) => {

  const [timeLeft, setTimeLeft] = useState(10 * 60);
  const [activeTab] = useState(1);
  const [time, setTime] = useState("10:00");

  function secondsToTime(totalSeconds) {
    const minutes = Math.floor(totalSeconds / (60));
    const seconds = totalSeconds % 60;
    console.log("total seconds", totalSeconds);
    console.log("mins", minutes);
    console.log("secs", seconds);
    return (minutes < 10 ? "0" + minutes : minutes) + ":" + (seconds < 10 ? "0" + seconds : seconds);
  }

  useImperativeHandle(ref, () => ({
    countDown() {
      setTimeLeft(timeLeft - 1);
      setTime(secondsToTime(timeLeft));
      if (timeLeft <= 0) {
        console.log("Game ended");
      }
    }
  }));

  return (
    <Grid container item justifyContent="center" xs={12} md={10} lg={10} xl={10} mx="auto" py={2} px={1}>
      <AppBar position="static" style={{ backgroundColor: "gray !important" }}>
        <Tabs value={activeTab}>
          {/* eslint-disable-next-line react/prop-types */}
          <Tab style={{ fontSize: 22, background: "antiquewhite" }} label={props.opponentName} />
          <Tab style={{ fontSize: 28, fontWeight: "bold", background: "white" }} label={time} />
        </Tabs>
      </AppBar>
    </Grid>
  );
});

export default OpponentInfo;