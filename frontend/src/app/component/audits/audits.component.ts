import {Component, OnInit} from '@angular/core';
import {SimService} from '../../service/sim.service';
import {Audits} from '../../model/audits';
import {SimComponent} from '../sim/sim.component';
import {ActivatedRoute, Router} from '@angular/router';
import {Location} from '@angular/common';

const SIZE = 10;

@Component({
  selector: 'app-audits',
  templateUrl: './audits.component.html',
  styleUrls: ['./audits.component.css']
})
export class AuditsComponent implements OnInit {

  private simAudits: Audits[];
  page = 0;
  totalPages: number[];
  value: number;

  constructor(private simService: SimService,
              private simComponent: SimComponent,
              private route: ActivatedRoute,
              private router: Router,
              private location: Location) {
  }

  goBack() {
    this.location.back();
  }

  setPage(i) {
    this.page = i;
    this.findAllPaginatedSimAuditsBySimId();
  }

  findAllPaginatedSimAuditsBySimId() {
    this.simService.findAllPaginatedSimAuditsBySimId(this.page, SIZE, this.value).subscribe(audit => {
      this.simAudits = audit.content;
      this.totalPages = Array(audit.totalPages).fill(0);
    }, error => {
      if (error.status !== 404) {
        localStorage.clear();
        this.router.navigateByUrl('/login');
      }
    });
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.value = params.id;
    });
    this.findAllPaginatedSimAuditsBySimId();
  }
}
