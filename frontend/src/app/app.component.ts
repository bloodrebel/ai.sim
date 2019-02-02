import {Component} from '@angular/core';
import {LoginService} from './service/login.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent {
  title = 'Ai.Sim';

  constructor(private loginService: LoginService, private router: Router) {
  }

  isLoged(): boolean {
    if (localStorage.getItem('id') === null) {
      return false;
    }
    return true;
  }

  logout(): void {
    this.loginService.logout().subscribe(data => {
      localStorage.clear();
      this.router.navigateByUrl('/login');
      console.log('Moderator has loged out');
    }, error => {
      localStorage.clear();
      this.router.navigateByUrl('/login');
      console.log('Moderator has loged out');
    });
  }


}
