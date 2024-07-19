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
import React, { useEffect, useState } from "react";
import { baseUrl, firebaseHost } from "../../../LandingPages/SignIn";
import { getDatabase, onValue, ref } from "firebase/database";
import GameOptions from "../../sections/GameOptions";
import { useNavigate } from "react-router-dom";

export const environment = "remote";
export const fetchWithCallBack = (url: string, params: any, callback: Function, on401: Function) => {
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

export const findByUser = (callback, on401) => {
  console.log("Fetching game by user:", localStorage.getItem("username"));
  return fetchWithCallBack(
    `${baseUrl}/chess?environment=${environment}`,
    {
      method: "GET",
      headers: headersWithAuth()
    },
    callback,
    on401
  );
};

export const findAllUsers = (callback, on401) => {
  return fetchWithCallBack(
    `${baseUrl}/user?environment=${environment}`,
    {
      method: "GET",
      headers: headersWithAuth()
    },
    callback,
    on401
  );
};

export const headersWithAuth = () => {
  return {
    "Content-type": "application/json",
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Methods": "POST",
    "Access-Control-Allow-Headers": "Origin, X-Requested-With, Content-Type, Accept, Authorization",
    "Authorization": `Basic ${localStorage.getItem("auth")}`
  };
};

const ChessGame = () => {

  const sendMove = (game: any, moveId: any | null, environment: string | null, callBack, on401, crowningTo?: number) => {
    let url = `${baseUrl}/chess/move/${moveId}?${crowningTo ? `&crowningTo=${crowningTo}` : ""}`;
    fetchWithCallBack(
      url,
      {
        method: "POST",
        headers: headersWithAuth(),
        body: JSON.stringify(game)
      },
      callBack,
      on401
    );
  };

  const player = localStorage.getItem("username");
  // const blackInDanger = false;
  // const whiteInDanger = false;
  // const size = "2.14em";
  const size = "10vh";
  const light = "#aa9898";
  const dark = "#287c22";
  const [game, setGame] = useState();
  // const colors = [light, dark];
  const [rows, setRows] = useState([]);

  const [selected, setSelected] = useState(null);

  function isValidMove(srcRow, srcCol, destRow, destCol) {
    return getPossibleMovesFrom(srcRow, srcCol)
      .filter(move => move.destRow === destRow && move.destCol === destCol)
      .length > 0;
  }

  function getPossibleMovesFrom(row, col) {
    if (game.possibleMoves) {
      return game
        .possibleMoves
        .filter(move => move.srcRow === row && move.srcCol === col);
    }
    return [];
  }

  function canMoveFrom(row, col) {
    return getPossibleMovesFrom(row, col).length > 0;
  }

  function selectingPiece() {
    return selected === null;
  }

  function myTurn() {
    return game.currentPlayer === localStorage.getItem("username");
  }

  const getMove = (row, col, row2, col2) => {
    let filter = getPossibleMovesFrom(row, col).filter(move => move.destRow === row2 && move.destCol === col2);
    return filter[0];
  };
  const clickHandler = (piece, row, col) => {
    console.log("player", player);
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
            game,
            game.possibleMoves.indexOf(getMove(selected.row, selected.col, row, col)),
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

  const [pieceSize, setSize] = useState(window.innerWidth / 8);
  const getPiece = (row, col) => {
    const piece = game.board.rows[row].spots[col].piece;
    const spotColor = getSpotColor(row, col);
    const color = piece <= 5 ? "#ffffff" : (piece <= 11 ? "black" : spotColor);
    if (selected && (selected.row === row && selected.col === col)) {
      console.log("TODO: Mark this spot as selected");
    }
    const pieceStyle = {
      margin: "0.5px",
      padding: "3.0px",
      color: color,
      backgroundColor: spotColor,
      width: pieceSize,
      height: pieceSize
    };
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

  const listenToPlayersTurn = (game) => {
    console.log("Listening", baseUrl);
    console.log("Listening", firebaseHost);
    console.log("Listening", process.env.FIREBASE_HOST);
    const db = getDatabase();
    let path = "chess/" + game.refId;
    const dbRef = ref(db, path);
    onValue(dbRef, (snapshot) => {
      if (game !== null) {
        setUpGame(snapshot.val(), "firebase");
      } else {
        console.log("No game found with the specified id");
      }
    });

  };

  function setUpGame(game, src) {
    console.log("Data source", src);
    if (game === null) {
      return;
    }
    setGame(game);
    setRows({ ...(game.board.rows) });
  }

  const nav = useNavigate();

  useEffect(() => {

    const resizeObserver = new ResizeObserver((event) => {
      setSize(event[0].borderBoxSize[0].inlineSize / 8.3);
    });

    resizeObserver.observe(document.getElementById("div1"));
    findByUser(
      (game) => {
        setUpGame(game, "service");
        listenToPlayersTurn(game);
      },
      (error) => {
        console.log(error);
      });
  }, []);

  let resign = () => {
    fetch(`${baseUrl}/chess?environment=${environment}`, { method: "DELETE", headers: headersWithAuth() })
      .then(() => {
        nav(`/`);
      });
  };
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
          <div
            id={`div1`}
            className="masonry-with-columns-2"
            style={{ opacity: 1 }}
          >
            {
              (game && game.board.rows) && <>
                {rows && game.board.rows.map((row) => {
                  let black = game.whitePlayer.username;
                  return (
                    row.spots.map((spot) => (
                      getPiece(player === black ? 7 - spot.row : spot.row, player !== black ? 7 - spot.col : spot.col)
                    ))
                  );
                })}
              </>
            }
          </div>
          <GameOptions onResign={resign} />
        </Container>
      </MKBox>
    </>
  );
};

export default ChessGame;
