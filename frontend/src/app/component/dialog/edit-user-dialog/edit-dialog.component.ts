import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {User} from '../../../model/user';
import {UserService} from '../../../service/user.service';


@Component({
  selector: 'app-edit-dialog',
  templateUrl: './edit-dialog.component.html',
  styleUrls: ['./edit-dialog.component.css']
})
export class EditDialogComponent {

  errorMessage: string;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: User,
    public dialogRef: MatDialogRef<EditDialogComponent>,
    public userService: UserService,
  ) {
    this.dialogRef.disableClose = true;
  }

  onNoClick(): void {
    this.dialogRef.close('closed');
  }

  edit(user: User): void {
    this.userService.editUser(user).subscribe(data => {
        console.log(data);
        this.dialogRef.close();
      },
      err => this.errorMessage = 'Invalid form data, please make sure you entered proper e-mail and nickname');
  }

}
