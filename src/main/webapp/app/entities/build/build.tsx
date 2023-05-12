import React, { useState, useEffect } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IBuild } from 'app/shared/model/build.model';
import { getEntities, reset } from './build.reducer';

export const Build = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );
  const [sorting, setSorting] = useState(false);

  const buildList = useAppSelector(state => state.build.entities);
  const loading = useAppSelector(state => state.build.loading);
  const totalItems = useAppSelector(state => state.build.totalItems);
  const links = useAppSelector(state => state.build.links);
  const entity = useAppSelector(state => state.build.entity);
  const updateSuccess = useAppSelector(state => state.build.updateSuccess);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const resetAll = () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
  };

  useEffect(() => {
    resetAll();
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      resetAll();
    }
  }, [updateSuccess]);

  useEffect(() => {
    getAllEntities();
  }, [paginationState.activePage]);

  const handleLoadMore = () => {
    if ((window as any).pageYOffset > 0) {
      setPaginationState({
        ...paginationState,
        activePage: paginationState.activePage + 1,
      });
    }
  };

  useEffect(() => {
    if (sorting) {
      getAllEntities();
      setSorting(false);
    }
  }, [sorting]);

  const sort = p => () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
    setSorting(true);
  };

  const handleSyncList = () => {
    resetAll();
  };

  return (
    <div>
      <h2 id="build-heading" data-cy="BuildHeading">
        <Translate contentKey="automatedPerformanceTestingApp.build.home.title">Builds</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="automatedPerformanceTestingApp.build.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/build/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="automatedPerformanceTestingApp.build.home.createLabel">Create new Build</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          dataLength={buildList ? buildList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {buildList && buildList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="automatedPerformanceTestingApp.build.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('branch')}>
                    <Translate contentKey="automatedPerformanceTestingApp.build.branch">Branch</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('tag')}>
                    <Translate contentKey="automatedPerformanceTestingApp.build.tag">Tag</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('createdBy')}>
                    <Translate contentKey="automatedPerformanceTestingApp.build.createdBy">Created By</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('createdOn')}>
                    <Translate contentKey="automatedPerformanceTestingApp.build.createdOn">Created On</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('updatedBy')}>
                    <Translate contentKey="automatedPerformanceTestingApp.build.updatedBy">Updated By</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('updatedOn')}>
                    <Translate contentKey="automatedPerformanceTestingApp.build.updatedOn">Updated On</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {buildList.map((build, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/build/${build.id}`} color="link" size="sm">
                        {build.id}
                      </Button>
                    </td>
                    <td>{build.branch}</td>
                    <td>{build.tag}</td>
                    <td>{build.createdBy}</td>
                    <td>{build.createdOn ? <TextFormat type="date" value={build.createdOn} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                    <td>{build.updatedBy}</td>
                    <td>{build.updatedOn ? <TextFormat type="date" value={build.updatedOn} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/build/${build.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`/build/${build.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`/build/${build.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                          <FontAwesomeIcon icon="trash" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.delete">Delete</Translate>
                          </span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            !loading && (
              <div className="alert alert-warning">
                <Translate contentKey="automatedPerformanceTestingApp.build.home.notFound">No Builds found</Translate>
              </div>
            )
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default Build;
