/*
=========================================================
* Material Kit 2 React - v2.1.0
=========================================================

* Product Page: https://www.creative-tim.com/product/material-kit-react
* Copyright 2023 Creative Tim (https://www.creative-tim.com)

Coded by www.creative-tim.com

 =========================================================

* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
*/

// @mui material components
import Container from "@mui/material/Container";
import Grid from "@mui/material/Grid";

// Material Kit 2 React components
import MKBox from "components/MKBox";
import MKTypography from "components/MKTypography";

// Material Kit 2 React examples
import DefaultNavbar from "examples/Navbars/DefaultNavbar";

// Presentation page sections
// Presentation page components
// Routes
import routes from "routes";

// Images
import bgImage from "assets/images/bg-presentation.jpg";
import React, { useEffect, useState } from "react";
import GameChallengeArea from "../../examples/Cards/BlogCards/CenteredBlogCard";
import { findAllUsers, findByUser } from "./components/Game";
import { useNavigate } from "react-router-dom";

function Presentation() {

  const [users, setUsers] = useState([]);
  const [loaded, setLoaded] = useState(false);
  const nav = useNavigate();

  useEffect(() => {
      console.log("fetching all users");
      if (!loaded) {
        findByUser((game) => {
          if (game) {
            nav(`/game`);
          }
        }, (error) => console.log(error));
        findAllUsers((users) => {
          setUsers(users.filter((user) => user.username !== localStorage.getItem("username")));
          setLoaded(true);
        }, (error) => console.log(error));
      }
    }
  );

  return (
    <>
      <DefaultNavbar
        routes={routes}
        action={{
          type: "external",
          route: "https://www.creative-tim.com/product/material-kit-react",
          label: "free download",
          color: "info"
        }}
        sticky
      />
      <MKBox
        minHeight="100vh"
        width="100%"
        sx={{
          backgroundImage: ({ functions: { linearGradient, rgba }, palette: { gradients } }) =>
            `${linearGradient(
              rgba(gradients.dark.main, 0.6),
              rgba(gradients.dark.state, 0.6)
            )}, url(${bgImage})`,
          backgroundSize: "cover",
          backgroundPosition: "center",
          display: "grid",
          placeItems: "center"
        }}
      >
        <Container>

          <Grid
            container
            item
            xs={12}
            lg={8}
            justifyContent="center"
            alignItems="center"
            flexDirection="column"
            sx={{ mx: "auto", textAlign: "center" }}
            style={{ background: "tan" }}
          >
            <MKTypography
              variant="h1"
              color="white"
              sx={({ breakpoints, typography: { size } }) => ({
                [breakpoints.down("md")]: {
                  fontSize: size["3xl"]
                }
              })}
            >
              Challenge online players to game.
            </MKTypography>
            {
              users.map(value => (
                <GameChallengeArea
                  key={value.username}
                  title={value.username}
                  description=""
                  action={{
                    type: "internal",
                    route: "pages/company/about-us",
                    color: "info",
                    label: "Challenge to game"
                  }}
                  image={""} />
              ))
            }

            <MKTypography variant="h6" color="white" mt={8} mb={1}>
              Find us on
            </MKTypography>
            <MKBox display="flex" justifyContent="center" alignItems="center">
              <MKTypography component="a" variant="body1" color="white" href="#" mr={3}>
                <i className="fab fa-facebook" />
              </MKTypography>
              <MKTypography component="a" variant="body1" color="white" href="#" mr={3}>
                <i className="fab fa-instagram" />
              </MKTypography>
              <MKTypography component="a" variant="body1" color="white" href="#" mr={3}>
                <i className="fab fa-twitter" />
              </MKTypography>
              <MKTypography component="a" variant="body1" color="white" href="#">
                <i className="fab fa-google-plus" />
              </MKTypography>
            </MKBox>
          </Grid>
        </Container>
      </MKBox>
    </>
  );
}

export default Presentation;
