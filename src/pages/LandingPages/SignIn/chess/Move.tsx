import {Capture} from "./Capture";

export interface Move {
    id: number | null
    piece: number
    srcRow: number
    srcCol: number
    destRow: number
    destCol: number
    capture: Capture
    isCrown: boolean
}