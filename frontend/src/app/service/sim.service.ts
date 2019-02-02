import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs/internal/Observable';
import {Sims} from '../model/sims';
import {PageableSims} from '../model/pageableSims';
import {PageableAudits} from '../model/pageableAudits';

@Injectable({
  providedIn: 'root'
})
export class SimService {

  private simsUrl = '/api/sims/';

  constructor(private http: HttpClient) {
  }

  getSims(page: number, size: number): Observable<PageableSims> {
    return this.http.get<PageableSims>(this.simsUrl + '?page=' + page + '&size=' + size).pipe();
  }

  getByMsisdn(msisdn: String): Observable<Sims> {
    return this.http.get<Sims>(this.simsUrl + 'msisdn/' + msisdn).pipe();
  }

  getByImsi(imsi: String): Observable<Sims> {
    return this.http.get<Sims>(this.simsUrl + 'imsi/' + imsi).pipe();
  }

  getBySimNumber(simNumber: String): Observable<Sims> {
    return this.http.get<Sims>(this.simsUrl + 'simNumber/' + simNumber).pipe();
  }

  findAllPaginatedSimAuditsBySimId(page: number, size: number, simId: number): Observable<PageableAudits> {
    return this.http.get<PageableAudits>(this.simsUrl + "history/?page=" + page +
      "&size=" + size + "&simId=" + simId).pipe();
  }

  getAllPaginatedOperatorSims(page: number, size: number, operator: string): Observable<PageableSims> {
    return this.http.get<PageableSims>(this.simsUrl + '?page=' + page + '&size=' +
      size + '&operator=' + operator).pipe();
  }

  getAllPaginatedServiceSims(page: number, size: number, simService: string): Observable<PageableSims> {
    return this.http.get<PageableSims>(this.simsUrl + '?page=' + page + '&size=' +
      size + '&simService=' + simService).pipe();
  }

  getAllPaginatedEnvironmentSims(page: number, size: number, environment: string): Observable<PageableSims> {
    return this.http.get<PageableSims>(this.simsUrl + '?page=' + page + '&size=' +
      size + '&environment=' + environment).pipe();
  }

  getAllPaginatedStateSims(page: number, size: number, simState: string): Observable<PageableSims> {
    return this.http.get<PageableSims>(this.simsUrl + '?page=' + page + '&size=' +
      size + '&simState=' + simState).pipe();
  }

  getAllPaginatedUserSims(page: number, size: number, nickname: string): Observable<PageableSims> {
    return this.http.get<PageableSims>(this.simsUrl + '?page=' + page + '&size=' +
      size + '&nickname=' + nickname).pipe();
  }

  create(sim: Sims): Observable<Sims> {
    return this.http.post<Sims>(this.simsUrl, sim).pipe();
  }

  edit(sim: Sims): Observable<Sims> {
    return this.http.put<Sims>(this.simsUrl + sim.id, sim).pipe();
  }

  deleteSim(simId: number): Observable<void> {
    return this.http.delete<void>(this.simsUrl + simId).pipe();
  }
}
