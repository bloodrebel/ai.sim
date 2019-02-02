import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material";
import {Sims} from "../../../model/sims";
import {MatDialogRef} from "@angular/material/dialog";
import {SimService} from "../../../service/sim.service";
import {User} from "../../../model/user";
import {UserService} from "../../../service/user.service";

@Component({
  selector: 'app-edit-sim-dialog',
  templateUrl: './edit-sim-dialog.component.html',
  styleUrls: ['./edit-sim-dialog.component.css']
})
export class EditSimDialogComponent implements OnInit {

  errorMessage: string;
  users: User[];

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: Sims,
    public dialogRef: MatDialogRef<EditSimDialogComponent>,
    public simService: SimService,
    public userService: UserService
  ) {
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  edit(sim: Sims): void {
    this.simService.edit(sim).subscribe(data => {
        this.dialogRef.close();
      },
      err => this.errorMessage = err.error.message);
  }

  ngOnInit(): void {
    this.userService.getAllUsers().subscribe(d => {
      this.users = d;
    })
  }
}
