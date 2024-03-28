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

// Material Kit 2 React examples
import DefaultNavbar from "examples/Navbars/DefaultNavbar";

// Presentation page sections
// Presentation page components
// Routes
import routes from "routes";

// Images
import bgImage from "assets/images/bg-presentation.jpg";
import "index.css";
import { FaChessBishop, FaChessKing, FaChessKnight, FaChessPawn, FaChessQueen, FaChessRook } from "react-icons/fa";
import { useEffect, useState } from "react";
import { baseUrl } from "../../../LandingPages/SignIn";

const ChessGame = () => {
  const jsonHeaders = {
    "Content-type": "application/json",
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Methods": "POST",
    "Access-Control-Allow-Headers": "Origin, X-Requested-With, Content-Type, Accept, Authorization",
    "Authorization": `Basic ${localStorage.getItem("auth")}`
  };

  const fetchWithCallBack = (url: string, params: any, callback: Function, on401: Function) => {
    fetch(url, params)
      .then(response => {
        if (!response.ok) {
          console.log(response);
          throw Error(`${response.status}`);
        }
        return response.json();
      })
      .then(
        data => {
          callback(data);
        }
      )
      .catch((reason: Error) => {
        if (reason.message === "401") {
          on401();
        }
      });
  };

  const findById = (gameId, environment, callback, on401) => {
    return fetchWithCallBack(
      `${baseUrl}/chess/${gameId}?environment=${environment}`,
      {
        method: "GET",
        headers: jsonHeaders
      },
      callback,
      on401
    );
  };

  const sendMove = (gameId: string, move: any | null, environment: string | null, callBack, on401, crowningTo?: number) => {
    let url = `${baseUrl}/chess/move/${gameId}?environment=${environment}${crowningTo ? `&&crowningTo=${crowningTo}` : ""}`;
    fetchWithCallBack(
      url,
      {
        method: "POST",
        headers: jsonHeaders,
        body: JSON.stringify(move)
      },
      callBack,
      on401
    );
  };

  const player = "1";
  // const blackInDanger = false;
  // const whiteInDanger = false;
  // const size = "2.14em";
  const size = "10vh";
  const light = "#d8cfca";
  const dark = "#495057";
  const [game, setGame] = useState();
  // const colors = [light, dark];
  const [rows, setRows] = useState([]);
  const gameId = "default";
  const environment = "local";

  let style = {
    margin: "1.2px",
    padding: "1.8px",
    width: "100px"
  };

  const [selected, setSelected] = useState(null);

  function isValidMove(srcRow, srcCol, destRow, destCol) {
    return getPossibleMovesFrom(srcRow, srcCol)
      .filter(move => move.destRow === destRow && move.destCol === destCol)
      .length > 0;
  }

  function getPossibleMovesFrom(row, col) {
    return game
      .possibleMoves
      .filter(move => move.srcRow === row && move.srcCol === col);
  }

  function canMoveFrom(row, col) {
    return getPossibleMovesFrom(row, col).length > 0;
  }

  function selectingPiece() {
    return selected === null;
  }

  function myTurn() {
    return game.currentPlayer === parseInt(player);
  }

  const getMove = (row, col, row2, col2) => {
    let filter = getPossibleMovesFrom(row, col).filter(move => move.destRow === row2 && move.destCol === col2);
    return filter[0];
  };
  const clickHandler = (piece, row, col) => {
    if (myTurn()) {
      console.log("Clicked ", piece, row, col);
      if (selectingPiece()) {
        if (canMoveFrom(row, col)) {
          setSelected({ row: row, col: col });
        }
      } else {
        //placing piece
        if (isValidMove(selected.row, selected.col, row, col)) {
          console.log("Applicable:", isValidMove(selected.row, selected.col, row, col));
          sendMove(
            gameId,
            getMove(selected.row, selected.col, row, col),
            environment,
            (game) => setGame(game),
            () => {
            }
          )
          ;
        }
        setSelected(null);
      }
    }
  };

  function getSpotColor(row, col) {
    if (selected) {
      if ((selected.row === row && selected.col === col) || isValidMove(selected.row, selected.col, row, col)) {
        return "deepskyblue";
      }
    }
    return (row % 2 === col % 2 ? dark : light);
  }

  const getPiece = (row, col) => {
    console.log("Getting piece");
    const piece = game.board.rows[row].spots[col].piece;
    const spotColor = getSpotColor(row, col);
    const color = piece <= 5 ? "white" : (piece <= 11 ? "black" : spotColor);
    if (selected && (selected.row === row && selected.col === col)) {
      console.log("TODO: Mark this spot as selected");
    }
    const pieceStyle = { ...style, color: color, backgroundColor: spotColor };
    switch (piece) {
      case 0:
      case 6:
        return <FaChessRook
          key={row + "-" + col}
          className={`chess-piece white`}
          size={size}
          style={pieceStyle}
          onClick={() => clickHandler(piece, row, col)}
        />;
      case 1:
      case 7:
        return <FaChessKnight
          key={row + "-" + col}
          className={`chess-piece white`}
          size={size}
          style={pieceStyle}
          onClick={() => clickHandler(piece, row, col)}
        />;
      case 2:
      case 8:
        return <FaChessBishop
          key={row + "-" + col}
          className={`chess-piece white`}
          size={size}
          style={pieceStyle}
          onClick={() => clickHandler(piece, row, col)}
        />;
      case 3:
      case 9:
        return <FaChessQueen
          key={row + "-" + col}
          className={`chess-piece white`}
          size={size}
          style={pieceStyle}
          onClick={() => clickHandler(piece, row, col)}
        />;
      case 4:
      case 10:
        return <FaChessKing
          key={row + "-" + col}
          className={`chess-piece white`}
          size={size}
          style={pieceStyle}
          onClick={() => clickHandler(piece, row, col)}
        />;
      case 5:
      case 11:
        return <FaChessPawn
          key={row + "-" + col}
          className={`chess-piece white`}
          size={size}
          style={pieceStyle}
          onClick={() => clickHandler(piece, row, col)}
        />;
      default :
        return <FaChessPawn
          key={row + "-" + col}
          className={`chess-piece white`}
          size={size}
          style={pieceStyle}
          onClick={() => clickHandler(piece, row, col)}
        />;
    }
  };

  useEffect(() => {
    findById(
      gameId,
      environment,
      (game) => {
        setGame(game);
        alert("ROWS: " + JSON.stringify(game.board.rows));
        setRows({ ...(game.board.rows) });
        console.log("game", game);
      },
      (error) => {
        console.log(error);
      });
  }, []);

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
            flexDirection="row"
            sx={{ mx: "auto", textAlign: "center" }}
          >
            <div className="row masonry-with-columns-2" id="masonry-with-columns-2">
              {
                (game && game.board.rows) && <>
                  {rows && game.board.rows.map((row) => (
                    row.spots.map((spot) => (
                      getPiece(player === "0" ? 7 - spot.row : spot.row, player !== "0" ? 7 - spot.col : spot.col)
                    ))
                  ))}
                </>
              }
            </div>
          </Grid>
        </Container>
      </MKBox>
    </>
  );
};

export default ChessGame;
