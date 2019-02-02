import {Audits} from "./audits";

export interface PageableAudits {
  content: Audits[],
  totalPages: number
}
