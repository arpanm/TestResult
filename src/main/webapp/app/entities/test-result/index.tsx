import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestResult from './test-result';
import TestResultDetail from './test-result-detail';
import TestResultUpdate from './test-result-update';
import TestResultDeleteDialog from './test-result-delete-dialog';

const TestResultRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TestResult />} />
    <Route path="new" element={<TestResultUpdate />} />
    <Route path=":id">
      <Route index element={<TestResultDetail />} />
      <Route path="edit" element={<TestResultUpdate />} />
      <Route path="delete" element={<TestResultDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestResultRoutes;
