import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from '../model/user';
import {tap} from 'rxjs/operators';
import {UserPaged} from '../model/user-paged';

const size = 15;

@Injectable({
  providedIn: 'root'
})
export class UserService {


  constructor(private http: HttpClient) {
  }

  getAllUsers(team?: string): Observable<User[]> {
    if (team) {
      return this.http.get<User[]>('/api/user/all?team=' + team).pipe(tap(data => console.log(JSON.stringify(data))));
    }
    return this.http.get<User[]>('/api/user/all').pipe(tap(data => console.log(JSON.stringify(data))));
  }

  getAllPagedUsers(page: number, team?: string): Observable<UserPaged> {
    if (team) {
      return this.http.get<UserPaged>('/api/user/all?page=' + page + '&size=' + size + '&team=' + team)
        .pipe(tap(data => console.log(JSON.stringify(data))));
    }
    return this.http.get<UserPaged>('/api/user/all?page=' + page + '&size=' + size)
      .pipe(tap(data => console.log(JSON.stringify(data))));
  }

  postUser(userCreated: User): Observable<User> {
    return this.http.post<User>('/api/user/create', userCreated);
  }

  editUser(userEdit: User): Observable<User> {
    return this.http.put<User>('/api/user/' + userEdit.id, userEdit);
  }

  deleteUser(userDelete: number): Observable<User> {
    return this.http.delete<User>('/api/user/' + userDelete);
  }
}



