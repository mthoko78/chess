import React, { forwardRef, useImperativeHandle, useState } from "react";

// @mui material components
import Grid from "@mui/material/Grid";

// Material Kit 2 React components
import Tab from "@mui/material/Tab";
import Tabs from "@mui/material/Tabs";
import AppBar from "@mui/material/AppBar";

import {calculateTimeSpent, secondsToTime, GAME_DURATION} from "./GameOptions"

const OpponentInfo = forwardRef((props, ref) => {
  // eslint-disable-next-line react/prop-types
  const game = props.game;
  // eslint-disable-next-line react/prop-types
  const opponentName = props.opponentName;

  const timeAsString = secondsToTime(GAME_DURATION - calculateTimeSpent(game, opponentName));

  const [timeLeft, setTimeLeft] = useState(GAME_DURATION - calculateTimeSpent(game, opponentName));
  const [activeTab] = useState(1);

  useImperativeHandle(ref, () => ({
    countDown() {
      const newTime = timeLeft - 1;
      if (newTime <= 0) {
        console.log("Game ended");
      }
      else {
        setTimeLeft(newTime);
      }
    }
  }));

  return (
    <Grid container item justifyContent="center" xs={12} md={10} lg={10} xl={10} mx="auto" py={2} px={1}>
      <AppBar position="static" style={{ backgroundColor: "gray !important" }}>
        <Tabs value={activeTab}>
          <Tab style={{ fontSize: 22, background: "antiquewhite" }} label={opponentName} />
          <Tab style={{ fontSize: 28, fontWeight: "bold", background: "white" }} label={timeAsString} />
        </Tabs>
      </AppBar>
    </Grid>
  );
});

export default OpponentInfo;