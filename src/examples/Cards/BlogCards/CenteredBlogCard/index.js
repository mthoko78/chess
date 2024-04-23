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

// react-router components

// prop-types is a library for typechecking of props
import PropTypes from "prop-types";

// @mui material components
import Card from "@mui/material/Card";
import MuiLink from "@mui/material/Link";

// Material Kit 2 React components
import MKBox from "components/MKBox";
import MKTypography from "components/MKTypography";
import MKButton from "components/MKButton";
import { useNavigate } from "react-router-dom";
import { environment, fetchWithCallBack } from "../../../../pages/Presentation/components/Game";
import { baseUrl } from "../../../../pages/LandingPages/SignIn";

const CenteredBlogCard = ({ title, action }) => {
  const nav = useNavigate();
  const challengeToGame = (e) => {
    console.log("New challenge to ", title);

    fetchWithCallBack(
      `${baseUrl}/chess/create?opponent=${title}&environment=${environment}`,
      {
        method: "POST",
        headers: {
          "Authorization": `Basic ${localStorage.getItem("auth")}`,
        }
      },
      (data) => {
        console.log(data);
        nav("game?environment=local");
      },
      () => {
        nav("presentation");
      }
    );

    nav("");
    return e.preventDefault();
  };

  return (
    <Card style={{ marginBottom: "2px" }}>
      <MKBox position="relative" borderRadius="md" mx={2} mt={-3}>
        <MKBox
          component="img"
          src={""}
          alt={""}
          borderRadius="lg"
          width="100%"
          position="relative"
          zIndex={1}
        />
      </MKBox>
      <MKBox p={3} mt={-1} textAlign="center">
        <MKTypography display="inline" variant="h5" textTransform="capitalize" fontWeight="regular">
          {title + " "}
        </MKTypography>
        <MKButton
          component={MuiLink}
          href={action.route}
          target="_blank"
          rel="noreferrer"
          variant="gradient"
          size="small"
          color={action.color ? action.color : "dark"}
          onClick={(e) => challengeToGame(e)}
        >
          {action.label}
        </MKButton>
      </MKBox>
    </Card>
  );
};

// Typechecking props for the CenteredBlogCard
CenteredBlogCard.propTypes = {
  image: PropTypes.string.isRequired,
  title: PropTypes.string.isRequired,
  description: PropTypes.string.isRequired,
  action: PropTypes.shape({
    type: PropTypes.oneOf(["external", "internal"]).isRequired,
    route: PropTypes.string.isRequired,
    color: PropTypes.oneOf([
      "primary",
      "secondary",
      "info",
      "success",
      "warning",
      "error",
      "dark",
      "light"
    ]),
    label: PropTypes.string.isRequired
  }).isRequired
};

export default CenteredBlogCard;
