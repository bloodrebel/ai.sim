import {Component, OnInit} from '@angular/core';
import {LoginService} from '../../service/login.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  username: string;
  password: string;
  rePassword: string;
  role: string;
  message: string;

  constructor(private loginService: LoginService, private router: Router) {
  }

  tryRegister() {
    if (this.password !== this.rePassword) {
      this.message = 'Please type same password';
      return;
    }
    this.loginService.register(this.username, this.password, this.role).subscribe(() => {
    }, error => {
      if (error.status === '409') {
        this.message = 'User with that username already exists';
      } else {
        this.message = 'Invalid register credentials';
      }
    }, () => {
      this.router.navigateByUrl('/login');
    });

  }

  ngOnInit() {
    if (localStorage.getItem('id')) {
      this.router.navigateByUrl('/sims');
    }
    this.username = '';
    this.password = '';
    this.rePassword = '';
    this.role = 'ROLE_NORMAL';
  }

}
