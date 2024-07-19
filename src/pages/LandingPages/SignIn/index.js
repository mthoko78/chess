/**
 =========================================================
 * Material Kit 2 React - v2.1.0
 =========================================================

 * Product Page: https://www.creative-tim.com/product/material-kit-react
 * Copyright 2023 Creative Tim (https://www.creative-tim.com)

 Coded by www.creative-tim.com

 =========================================================

 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */
import React, { useEffect, useState } from "react";

// react-router-dom components
import { Link, useNavigate } from "react-router-dom";

// @mui material components
import Card from "@mui/material/Card";
import Switch from "@mui/material/Switch";
import Grid from "@mui/material/Grid";

// @mui icons
// Material Kit 2 React components
import MKBox from "components/MKBox";
import MKTypography from "components/MKTypography";
import MKInput from "components/MKInput";
import MKButton from "components/MKButton";

// Material Kit 2 React example components
import DefaultNavbar from "examples/Navbars/DefaultNavbar";
import SimpleFooter from "examples/Footers/SimpleFooter";

// Material Kit 2 React page layout routes
import routes from "routes";

// Images
import bgImage from "assets/images/bg-sign-in-basic.jpeg";
import { Buffer } from "buffer";
import { findByUser } from "../../Presentation/components/Game";

// eslint-disable-next-line no-undef
export const baseUrl = process.env.REACT_APP_BASE_URL.split(",")[0];
export const firebaseHost = process.env.REACT_APP_BASE_URL.split(",")[1];

function SignInBasic() {
  const [rememberMe, setRememberMe] = useState(false);
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [password, setPassword] = useState("");
  const [register, setRegister] = useState(false);

  useEffect(() => {
    setEmail("");
    setPassword("");
  }, []);

  const handleSetRememberMe = () => setRememberMe(!rememberMe);

  function postLogin(user, redirectTo) {
    localStorage.setItem("auth", Buffer.from(`${user.username}:${user.password}`).toString("base64"));
    localStorage.setItem("username", user.username);

    findByUser(
      (game) => {
        console.log(game);
        if (game !== null) {
          redirectTo = `/game`;
        } else if (redirectTo === null) {
          redirectTo = `/`;
        }
        navigate(redirectTo);
      },
      (error) => {
        alert("Could not load game, please try again later");
        console.log(error);
      }
    );
  }

  const login = (user, redirectTo) => {
    console.log(baseUrl);
    fetch(`${baseUrl}/user/current`, {
      method: "POST",
      headers: {
        Authorization: `Basic ${Buffer.from(`${user.username}:${user.password}`).toString("base64")}`
      }
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else {
          throw Error(response);
        }
      })
      .then((user) => {
        postLogin(user, redirectTo);
      })
      .catch((error: Error) => {
        console.log(error);
        alert("Error:" + error);
      });
  };

  const send = () => {
    if (register) {
      signUp();
    } else {
      signIn();
    }
  };

  const signIn = () => {
    console.log(navigate);
    login({ username: email, password: password }, `/chess/default/0`);
  };

  const signUp = () => {
    return fetch(`${baseUrl}/user/register`, {
      method: "POST",
      headers: {
        "Content-type": "application/json"
      },
      body: JSON.stringify({ username: email, password: password })
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else {
          throw Error(response.status.toString());
        }
      })
      .then(() => {
        alert(`Account created successfully`);
        signIn();
      })
      .catch((error) => {
        console.log("Could not register", error.message);
      });
  };

  return (
    <>
      <DefaultNavbar
        routes={routes}
        action={{
          type: "external",
          route: "https://www.creative-tim.com/product/material-kit-react",
          label: "",
          color: "info"
        }}
        transparent
        light
      />
      <MKBox
        position="absolute"
        top={0}
        left={0}
        zIndex={1}
        width="100%"
        minHeight="100vh"
        sx={{
          backgroundImage: ({ functions: { linearGradient, rgba }, palette: { gradients } }) =>
            `${linearGradient(
              rgba(gradients.dark.main, 0.6),
              rgba(gradients.dark.state, 0.6)
            )}, url(${bgImage})`,
          backgroundSize: "cover",
          backgroundPosition: "center",
          backgroundRepeat: "no-repeat"
        }}
      />
      <MKBox px={1} width="100%" height="100vh" mx="auto" position="relative" zIndex={2}>
        <Grid container spacing={1} justifyContent="center" alignItems="center" height="100%">
          <Grid item xs={11} sm={9} md={5} lg={4} xl={3}>
            <Card>
              <MKBox
                variant="gradient"
                bgColor="info"
                borderRadius="lg"
                coloredShadow="info"
                mx={2}
                mt={-3}
                p={2}
                mb={1}
                textAlign="center"
              >
                <MKTypography variant="h4" fontWeight="medium" color="white" mt={1}>
                  {register ? `Register` : `Sign in`}
                </MKTypography>
              </MKBox>
              <MKBox pt={4} pb={3} px={3}>
                <MKBox component="form" role="form">
                  {register && (
                    <MKBox mb={2}>
                      <MKInput
                        id={"name"}
                        autoComplete={"false"}
                        type="text"
                        label="Name"
                        fullWidth
                        value={username}
                        onChange={(e) => {
                          setUsername(e.target.value);
                        }}
                      />
                    </MKBox>
                  )}
                  <MKBox mb={2}>
                    <MKInput
                      id={"email"}
                      autoComplete={"false"}
                      type="email"
                      label="Email"
                      fullWidth
                      value={email}
                      onChange={(e) => {
                        setEmail(e.target.value);
                      }}
                    />
                  </MKBox>
                  <MKBox mb={2}>
                    <MKInput
                      id={"password"}
                      type="password"
                      label="Password"
                      fullWidth
                      value={password}
                      onChange={(e) => {
                        setPassword(e.target.value);
                      }}
                    />
                  </MKBox>
                  <MKBox mb={2}>
                    {register && (
                      <MKInput
                        id={"confirmPassword"}
                        type="password"
                        label="Confirm password"
                        fullWidth
                        value={confirmPassword}
                        onChange={(e) => {
                          setConfirmPassword(e.target.value);
                        }}
                      />
                    )}
                  </MKBox>
                  <MKBox display="flex" alignItems="center" ml={-1}>
                    <Switch id={"rememberMe"} checked={rememberMe} onChange={handleSetRememberMe} />
                    <MKTypography
                      variant="button"
                      fontWeight="regular"
                      color="text"
                      onClick={handleSetRememberMe}
                      sx={{ cursor: "pointer", userSelect: "none", ml: -1 }}
                    >
                      &nbsp;&nbsp;Remember me
                    </MKTypography>
                  </MKBox>
                  <MKBox mt={4} mb={1}>
                    <MKButton style={{ cursor: "pointer" }} variant="gradient" color="info" fullWidth onClick={send}>
                      {register ? `register` : `sign in`}
                    </MKButton>
                  </MKBox>
                  <MKBox mt={3} mb={1} textAlign="center">
                    <MKTypography variant="button" color="text">
                      {register ? `Already have an account` : `Don't have an account`}?{" "}
                      <MKTypography
                        component={Link}
                        to="/authentication/sign-up/cover"
                        variant="button"
                        color="info"
                        fontWeight="medium"
                        textGradient
                        onClick={(e) => {
                          e.preventDefault();
                          setRegister(!register);
                        }}
                      >
                        {register ? `Sign in` : `Sign up`}
                      </MKTypography>
                    </MKTypography>
                  </MKBox>
                </MKBox>
              </MKBox>
            </Card>
          </Grid>
        </Grid>
      </MKBox>
      <MKBox width="100%" position="absolute" zIndex={2} bottom="1.625rem">
        <SimpleFooter light />
      </MKBox>
    </>
  );
}

export default SignInBasic;
