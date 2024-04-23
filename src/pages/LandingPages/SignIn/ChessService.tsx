import { fetchWithCallBack } from "pages/Presentation/components/Game";
import { Move } from "./chess/Move";

const jsonHeaders = {
  "Content-type": "application/json",
  "Access-Control-Allow-Origin": "*",
  "Access-Control-Allow-Methods": "*",
  "Access-Control-Allow-Headers": "Origin, X-Requested-With, Content-Type, Accept, Authorization",
};

export const baseUrl = process.env.REACT_APP_BASE_URL;

export const sendMove = (gameId: string, move: Move | null, environment: string | null, crowningTo?: number) => {
  let url = `${baseUrl}/chess/move/${gameId}?environment=${environment}${crowningTo ? `&&crowningTo=${crowningTo}` : ""}`;
  return fetch(url, {
    method: "POST",
    headers: jsonHeaders,
    body: JSON.stringify(move),
  });
};

export const resetGame = (gameId: string | undefined, environment: string | null) => {
  console.log(`Basic ${localStorage.getItem("auth")}`);
  return fetch(`${baseUrl}/chess/reset/${gameId}?environment=${environment}`, {
    headers: {
      "Authorization": `Basic ${localStorage.getItem("auth")}`,
    },
  });
};

export const handleResignation = (gameId: string, reason: string, environment: string | null) => {
  return fetch(`${baseUrl}/chess/resignation/${gameId}/${reason}?environment=${environment}`, { method: "POST" });
};

export const findById = (gameId: string | undefined, environment: string | null, callback: Function, on401: Function) => {
  return fetchWithCallBack(
    `${baseUrl}/chess/${gameId}?environment=${environment}`,
    {
      method: "GET",
      headers: {
        "Authorization": `Basic ${localStorage.getItem("auth")}`,
      },
    },
    callback,
    on401,
  );
};

export const createGame = (refId: string | undefined, environment: string | null) => {
  let url = `${baseUrl}/chess/create?environment=${environment}&id=${refId}`;
  return fetch(url, {
    method: "POST",
    headers: {
      "Authorization": `Basic ${localStorage.getItem("auth")}`,
    },
    body: "",
  });
};