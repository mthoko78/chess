import React, { useState } from "react";

// @mui material components
import Grid from "@mui/material/Grid";

// Material Kit 2 React components
import Tab from "@mui/material/Tab";
import Tabs from "@mui/material/Tabs";
import AppBar from "@mui/material/AppBar";

function DropdownAndDropup(props) {

  const resign = `Resign`;
  const offerDraw = `Offer draw`;
  const restart = `Restart game`;

  const closeDropdown = (option) => {
    switch (option) {
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

  return (

    <Grid container item justifyContent="center" xs={10} md={10} lg={10} xl={10} mx="auto" py={2} px={1}>
      <AppBar position="static">
        <Tabs value={activeTab} onChange={handleTabType}>
          <Tab label={resign} onClick={() => closeDropdown(resign)} />
          <Tab label={restart} onClick={() => closeDropdown(restart)} />
          <Tab label={offerDraw} onClick={() => closeDropdown(offerDraw)} />
        </Tabs>
      </AppBar>
    </Grid>
  );
}

export default DropdownAndDropup;