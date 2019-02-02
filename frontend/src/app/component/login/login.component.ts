import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {LoginService} from '../../service/login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  username: string;
  password: string;
  message: string;

  constructor(private loginService: LoginService, private router: Router) {
  }

  ngOnInit() {
    if (localStorage.getItem('id')) {
      this.router.navigateByUrl('/sims');
    }
  }

  tryLogin(): void {
    this.loginService.login(this.username, this.password).subscribe(data => {
        console.log('Moderator loged in:');
        localStorage.setItem('id', data.id.toString());
        localStorage.setItem('username', data.username);
        localStorage.setItem('password', data.password);
        localStorage.setItem('role', data.role);
        this.router.navigateByUrl('/sims');
      },
      error => this.message = 'Invalid user credentials');
  }
}
