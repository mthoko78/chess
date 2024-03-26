import {Board} from "./Board";
import {Move} from "./Move";

export default interface Game {
    id: number | null
    refId: string
    board: Board
    moves: Move[]
    possibleMoves: Move[]
    currentPlayer: number
    status: string
}