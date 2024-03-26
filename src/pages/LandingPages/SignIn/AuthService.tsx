import { Buffer } from "buffer";
import { baseUrl } from "./ChessService";

export const login = (user, redirectTo, nav) => {
  const encodedString = Buffer.from(`${user.username}:${user.password}`).toString("base64");
  console.log("USER:", user);
  console.log("AUTH:", encodedString);
  return fetch(`${baseUrl}/user/current`, {
    method: "POST",
    headers: {
      Authorization: `Basic ${encodedString}`,
    },
  })
    .then((response) => {
      if (response.ok) {
        return response.json();
      } else {
        throw Error(response.status.toString());
      }
    })
    .then((user1) => {
      localStorage.setItem("auth", encodedString);
      localStorage.setItem("sessionId", user1.sessionId);
      nav(redirectTo);
    })
    .catch((error) => {
      console.log("Could not login", error.message);
    });
};

export const register = (user, redirectTo, nav) => {
  return fetch(`${baseUrl}/user/register`, {
    method: "POST",
    headers: {
      "Content-type": "application/json",
    },
    body: JSON.stringify(user),
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
    })
    .catch((error) => {
      console.log("Could not register", error.message);
    });
};
