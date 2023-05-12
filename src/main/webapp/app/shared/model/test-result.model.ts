import dayjs from 'dayjs';
import { IBuild } from 'app/shared/model/build.model';

export interface ITestResult {
  id?: number;
  pageLoad?: number | null;
  timeToInteractive?: number | null;
  createdBy?: number | null;
  createdOn?: string | null;
  updatedBy?: number | null;
  updatedOn?: string | null;
  build?: IBuild | null;
}

export const defaultValue: Readonly<ITestResult> = {};
