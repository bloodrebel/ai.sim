import {User} from "./user";

export class Sims {

  id: number;
  msisdn: string;
  simNumber: string;
  imsi: string;
  operator: string;
  simService: string;
  environment: string;
  simState: string;
  user: User;

  constructor(id: number,
              msisdn: string,
              simNumber: string,
              imsi: string,
              operator: string,
              simService: string,
              environment: string,
              simState: string,
              user: User) {
  }

}
