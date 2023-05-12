import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './build.reducer';

export const BuildDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const buildEntity = useAppSelector(state => state.build.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="buildDetailsHeading">
          <Translate contentKey="automatedPerformanceTestingApp.build.detail.title">Build</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{buildEntity.id}</dd>
          <dt>
            <span id="branch">
              <Translate contentKey="automatedPerformanceTestingApp.build.branch">Branch</Translate>
            </span>
          </dt>
          <dd>{buildEntity.branch}</dd>
          <dt>
            <span id="tag">
              <Translate contentKey="automatedPerformanceTestingApp.build.tag">Tag</Translate>
            </span>
          </dt>
          <dd>{buildEntity.tag}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="automatedPerformanceTestingApp.build.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{buildEntity.createdBy}</dd>
          <dt>
            <span id="createdOn">
              <Translate contentKey="automatedPerformanceTestingApp.build.createdOn">Created On</Translate>
            </span>
          </dt>
          <dd>{buildEntity.createdOn ? <TextFormat value={buildEntity.createdOn} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedBy">
              <Translate contentKey="automatedPerformanceTestingApp.build.updatedBy">Updated By</Translate>
            </span>
          </dt>
          <dd>{buildEntity.updatedBy}</dd>
          <dt>
            <span id="updatedOn">
              <Translate contentKey="automatedPerformanceTestingApp.build.updatedOn">Updated On</Translate>
            </span>
          </dt>
          <dd>{buildEntity.updatedOn ? <TextFormat value={buildEntity.updatedOn} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/build" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/build/${buildEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BuildDetail;
