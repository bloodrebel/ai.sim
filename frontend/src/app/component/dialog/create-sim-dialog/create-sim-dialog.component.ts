import {Component, OnInit} from '@angular/core';
import {MatDialogRef} from "@angular/material";
import {SimService} from "../../../service/sim.service";
import {Sims} from "../../../model/sims";
import {User} from "../../../model/user";
import {UserService} from "../../../service/user.service";

@Component({
  selector: 'app-create-sim-dialog',
  templateUrl: './create-sim-dialog.component.html',
  styleUrls: ['./create-sim-dialog.component.css']
})
export class CreateSimDialogComponent implements OnInit {

  private sims: Sims = new Sims(null, '', '', '', '', '',
    '', '', null);

  private users: User[];

  errorMessage: string;

  constructor(
    public dialogRef: MatDialogRef<CreateSimDialogComponent>,
    public simService: SimService,
    public userService: UserService
  ) {
  }

  onCancelClick(): void {
    this.dialogRef.close();
  }

  create(sim: Sims): void {
    this.simService.create(sim).subscribe(data => {
        this.dialogRef.close();
      },
      err => this.errorMessage = err.error.message);
  }

  ngOnInit(): void {
    this.userService.getAllUsers().subscribe(data => {
      this.users = data;
    })
  }
}
