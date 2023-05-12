import dayjs from 'dayjs';
import { ITestResult } from 'app/shared/model/test-result.model';

export interface IBuild {
  id?: number;
  branch?: string | null;
  tag?: string | null;
  createdBy?: number | null;
  createdOn?: string | null;
  updatedBy?: number | null;
  updatedOn?: string | null;
  results?: ITestResult[] | null;
}

export const defaultValue: Readonly<IBuild> = {};
