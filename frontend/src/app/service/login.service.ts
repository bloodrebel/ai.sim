import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Moderator} from '../model/moderator';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private http: HttpClient) {
  }

  login(username: string, password: string): Observable<Moderator> {
    return this.http.post<Moderator>('/api/authenticate', {username: username, password: password});
  }

  logout() {
    return this.http.get('/api/logout');
  }

  register(username: string, password: string, role: string) {
    return this.http.post<Moderator>('/api/register', {username: username, password: password, role: role});
  }

}
