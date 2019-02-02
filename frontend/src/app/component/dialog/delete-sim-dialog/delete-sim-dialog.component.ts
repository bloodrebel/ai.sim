import {Component, Inject} from '@angular/core';
import {SimService} from "../../../service/sim.service";
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';

@Component({
  selector: 'app-delete-sim-dialog',
  templateUrl: './delete-sim-dialog.component.html',
  styleUrls: ['./delete-sim-dialog.component.css']
})
export class DeleteSimDialogComponent {

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: number,
    public dialogRef: MatDialogRef<DeleteSimDialogComponent>,
    public simService: SimService,
  ) {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  deleteUser(): void {
    this.simService.deleteSim(this.data).subscribe(data => {
      this.dialogRef.close();
    });
  }
}
