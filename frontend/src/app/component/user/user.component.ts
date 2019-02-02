import {Component, OnInit} from '@angular/core';
import {UserService} from '../../service/user.service';
import {User} from '../../model/user';
import {MatDialog} from '@angular/material';
import {CreateDialogComponent} from '../dialog/create-user-dialog/create-dialog.component';
import {EditDialogComponent} from '../dialog/edit-user-dialog/edit-dialog.component';
import {DeleteDialogComponent} from '../dialog/delete-user-dialog/delete-dialog.component';
import {UserPaged} from '../../model/user-paged';
import {Router} from '@angular/router';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  users: User[];
  fil: string = '';
  team: string = '';
  displayedUsers: User[];
  totalPages: number[];
  currentPage: number;

  constructor(private userService: UserService, private dialog: MatDialog, private router: Router) {
  }

  ngOnInit() {
    this.currentPage = 0;
    this.initData();
  }

  openCreate(): void {
    const dialogRef = this.dialog.open(CreateDialogComponent, {width: '450px', height: '520px'});

    dialogRef.afterClosed().subscribe(result => {
      if (result !== 'closed') {
        this.currentPage = 0;
        this.refreshData();
      }
    });
  }

  edit(user: User): void {
    const dialogRef = this.dialog.open(EditDialogComponent, {
      width: '450px',
      height: '520px',
      data: {id: user.id, firstName: user.firstName, lastName: user.lastName, nickName: user.nickName, eMail: user.eMail, team: user.team}
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result !== 'closed') {
        this.refreshData();
      }
    });
  }

  delete(id: number): void {
    const dialogRef = this.dialog.open(DeleteDialogComponent, {
      width: '450px', data: id
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result !== 'closed') {
        this.refreshData();
      }
    });

  }

  filter() {
    if (this.fil.replace(/\s/g, '') === '' || this.fil == null) {
      this.currentPage = 0;
      this.refreshData();
      return;
    }
    this.userService.getAllUsers(this.team).subscribe((data: User[]) => {
        this.displayedUsers = data.filter(user => user.nickName.indexOf(this.fil) > -1);
        this.totalPages = null;
      }
    );
  }

  refreshData(): void {
    this.userService.getAllPagedUsers(this.currentPage, this.team).subscribe((data: UserPaged) => {
      if (data) {
        this.users = data.content;
        this.displayedUsers = this.users;
        this.totalPages = Array(data.totalPages).fill(0);
      }
    }, error => {
      this.setPage(0);
    });
  }

  initData(): void {
    this.userService.getAllPagedUsers(this.currentPage, this.team).subscribe((data: UserPaged) => {
      if (data) {
        this.users = data.content;
        this.displayedUsers = this.users;
        this.totalPages = Array(data.totalPages).fill(0);
      }
    }, error => {
      localStorage.clear();
      this.router.navigateByUrl('/login');
    });
  }


  setPage(page: number): void {
    this.userService.getAllPagedUsers(page, this.team).subscribe((data: UserPaged) => {
      this.users = data.content;
      this.displayedUsers = this.users;
      this.currentPage = page;
      this.totalPages = Array(data.totalPages).fill(0);
    }, error => {
      this.totalPages = null;
      this.currentPage = 0;
      this.displayedUsers = null;
    });
  }

}
