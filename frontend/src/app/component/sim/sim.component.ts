import {Component, OnInit} from '@angular/core';
import {Sims} from '../../model/sims';
import {SimService} from '../../service/sim.service';
import {MatDialog} from '@angular/material/dialog';
import {Router} from '@angular/router';
import {CreateSimDialogComponent} from '../dialog/create-sim-dialog/create-sim-dialog.component';
import {DeleteSimDialogComponent} from '../dialog/delete-sim-dialog/delete-sim-dialog.component';
import {EditSimDialogComponent} from '../dialog/edit-sim-dialog/edit-sim-dialog.component';

const SIZE = 20;

@Component({
  selector: 'sim-root',
  templateUrl: './sim.component.html',
  styleUrls: ['./sim.component.css']
})

export class SimComponent implements OnInit {
  page = 0;
  filteredSims: Sims[] = [];
  selectedFilter = 'all';
  totalPages: number[];
  showPaging = true;

  constructor(private simService: SimService,
              private dialog: MatDialog,
              private router: Router
  ) {
  }

  private _listFilter: string;
  get listFilter(): string {
    return this._listFilter;
  }

  set listFilter(value: string) {
    this._listFilter = value;
  }

  search() {
    this.setPage(0);
    this.listFilter ? this.filtered(this.selectedFilter, this.listFilter) : this.getSims();
  }

  clearSearch() {
    this.listFilter = '';
  }

  setPage(i) {
    this.page = i;
    this.filtered(this.selectedFilter, this.listFilter);
  }

  getSims() {
    this.simService.getSims(this.page, SIZE).subscribe(
      sim => {
        if (sim) {
          this.filteredSims = sim.content;
          this.totalPages = Array(sim.totalPages).fill(0);
          this.showPaging = true;
        } else {
          this.filteredSims.length = 0;
          this.showPaging = false;
        }
      }, error => {
        localStorage.clear();
        this.router.navigateByUrl('/login');
      }
    );
  }

  getByMsisdn(msisdn: String) {
    this.showPaging = false;
    this.simService.getByMsisdn(msisdn).subscribe(
      sim => {
        if (sim) {
          this.filteredSims = Array.of(sim);
        } else {
          this.filteredSims.length = 0;
        }
      }
    );
  }

  getByImsi(imsi: String) {
    this.showPaging = false;
    this.simService.getByImsi(imsi).subscribe(
      sim => {
        if (sim) {
          this.filteredSims = Array.of(sim);
        } else {
          this.filteredSims.length = 0;
        }
        }
    );
  }

  getBySimNumber(simNumber: String) {
    this.showPaging = false;
    this.simService.getBySimNumber(simNumber).subscribe(
      sim => {
        if (sim) {
          this.filteredSims = Array.of(sim);
        } else {
          this.filteredSims.length = 0;
        }
      }
    );
  }

  getPaginatedOperatorSims(filterString: string) {
    this.simService.getAllPaginatedOperatorSims(this.page, SIZE, filterString).subscribe(
      sim => {
        if (sim) {
          this.filteredSims = sim.content;
          this.totalPages = Array(sim.totalPages).fill(0);
          this.showPaging = true;
        } else {
          this.filteredSims.length = 0;
          this.showPaging = false;
        }
      }
    );
  }

  getPaginatedServiceSims(filterString: string) {
    this.simService.getAllPaginatedServiceSims(this.page, SIZE, filterString).subscribe(
      sim => {
        if (sim) {
          this.filteredSims = sim.content;
          this.totalPages = Array(sim.totalPages).fill(0);
          this.showPaging = true;
        } else {
          this.filteredSims.length = 0;
          this.showPaging = false;
        }
      }
    );
  }

  getPaginatedEnvironmentSims(filterString: string) {
    this.simService.getAllPaginatedEnvironmentSims(this.page, SIZE, filterString).subscribe(
      sim => {
        if (sim) {
          this.filteredSims = sim.content;
          this.totalPages = Array(sim.totalPages).fill(0);
          this.showPaging = true;
        } else {
          this.filteredSims.length = 0;
          this.showPaging = false;
        }
      }
    );
  }

  getPaginatedStateSims(filterString: string) {
    this.simService.getAllPaginatedStateSims(this.page, SIZE, filterString).subscribe(
      sim => {
        if (sim) {
          this.filteredSims = sim.content;
          this.totalPages = Array(sim.totalPages).fill(0);
          this.showPaging = true;
        } else {
          this.filteredSims.length = 0;
          this.showPaging = false;
        }
      }
    );
  }

  getPaginatedUserSims(filterString: string) {
    this.simService.getAllPaginatedUserSims(this.page, SIZE, filterString).subscribe(
      sim => {
        if (sim) {
          this.filteredSims = sim.content;
          this.totalPages = Array(sim.totalPages).fill(0);
          this.showPaging = true;
        } else {
          this.filteredSims.length = 0;
          this.showPaging = false;
        }
      }
    );
  }

  create() {
    const dialogRef = this.dialog.open(CreateSimDialogComponent, {width: '450px', height: '520px'});

    dialogRef.afterClosed().subscribe(result => {
      this.page = 0;
      this.getSims();
    });
    this.clearSearch();
  }

  delete(simId: number) {
    const dialogRef = this.dialog.open(DeleteSimDialogComponent, {
      width: '450px', data: simId
    });

    dialogRef.afterClosed().subscribe(result => {
      this.getSims();
      this.setPage(0);
    });
    this.clearSearch();
  }

  edit(sim: Sims) {
    const dialogRef = this.dialog.open(EditSimDialogComponent, {
      width: '450px',
      height: '500px',
      data: {
        id: sim.id,
        msisdn: sim.msisdn,
        simNumber: sim.simNumber,
        imsi: sim.imsi,
        operator: sim.operator,
        simService: sim.simService,
        environment: sim.environment,
        simState: sim.simState,
        user: sim.user
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      this.getSims();
      this.clearSearch();
    });
  }

  onAuditsClick(simId: number) {
    this.router.navigateByUrl('audits/' + simId);
  }

  filtered(selectedFilter: string, filterString: string): void {
    switch (selectedFilter) {
      case "msisdn":
        this.getByMsisdn(filterString);
        break;
      case "imsi":
        this.getByImsi(filterString);
        break;
      case "simNum":
        this.getBySimNumber(filterString);
        break;
      case "operator":
        this.getPaginatedOperatorSims(filterString);
        break;
      case "service":
        this.getPaginatedServiceSims(filterString);
        break;
      case "environment":
        this.getPaginatedEnvironmentSims(filterString);
        break;
      case "state":
        this.getPaginatedStateSims(filterString);
        break;
      case "user":
        this.getPaginatedUserSims(filterString);
        break;
      default:
        this.getSims();
        break;
    }
  }

  ngOnInit(): void {
    this.getSims();
  }
}
