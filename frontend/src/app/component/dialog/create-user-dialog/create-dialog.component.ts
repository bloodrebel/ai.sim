import {Component} from '@angular/core';
import {MatDialogRef} from '@angular/material';
import {User} from '../../../model/user';
import {UserService} from '../../../service/user.service';


@Component({
  templateUrl: './create-dialog.component.html',
  styleUrls: ['./create-dialog.component.css']
})
export class CreateDialogComponent {

  private user: User = new User(null, '', '', '', '', '');

  errorMessage: string;

  constructor(
    public dialogRef: MatDialogRef<CreateDialogComponent>,
    public userService: UserService,
  ) {
    this.dialogRef.disableClose = true;
  }

  onNoClick(): void {
    this.dialogRef.close('closed');
  }

  create(user: User): void {
    this.userService.postUser(user).subscribe(data => {
        console.log(data);
        this.dialogRef.close();
      },
      err => this.errorMessage = 'Invalid form data, please make sure you entered proper e-mail and nickname');
  }
}
