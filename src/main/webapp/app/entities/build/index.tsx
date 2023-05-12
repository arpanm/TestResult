import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Build from './build';
import BuildDetail from './build-detail';
import BuildUpdate from './build-update';
import BuildDeleteDialog from './build-delete-dialog';

const BuildRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Build />} />
    <Route path="new" element={<BuildUpdate />} />
    <Route path=":id">
      <Route index element={<BuildDetail />} />
      <Route path="edit" element={<BuildUpdate />} />
      <Route path="delete" element={<BuildDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BuildRoutes;
