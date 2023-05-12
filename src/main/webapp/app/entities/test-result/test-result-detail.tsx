import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './test-result.reducer';

export const TestResultDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const testResultEntity = useAppSelector(state => state.testResult.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="testResultDetailsHeading">
          <Translate contentKey="automatedPerformanceTestingApp.testResult.detail.title">TestResult</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{testResultEntity.id}</dd>
          <dt>
            <span id="pageLoad">
              <Translate contentKey="automatedPerformanceTestingApp.testResult.pageLoad">Page Load</Translate>
            </span>
          </dt>
          <dd>{testResultEntity.pageLoad}</dd>
          <dt>
            <span id="timeToInteractive">
              <Translate contentKey="automatedPerformanceTestingApp.testResult.timeToInteractive">Time To Interactive</Translate>
            </span>
          </dt>
          <dd>{testResultEntity.timeToInteractive}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="automatedPerformanceTestingApp.testResult.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{testResultEntity.createdBy}</dd>
          <dt>
            <span id="createdOn">
              <Translate contentKey="automatedPerformanceTestingApp.testResult.createdOn">Created On</Translate>
            </span>
          </dt>
          <dd>
            {testResultEntity.createdOn ? (
              <TextFormat value={testResultEntity.createdOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedBy">
              <Translate contentKey="automatedPerformanceTestingApp.testResult.updatedBy">Updated By</Translate>
            </span>
          </dt>
          <dd>{testResultEntity.updatedBy}</dd>
          <dt>
            <span id="updatedOn">
              <Translate contentKey="automatedPerformanceTestingApp.testResult.updatedOn">Updated On</Translate>
            </span>
          </dt>
          <dd>
            {testResultEntity.updatedOn ? (
              <TextFormat value={testResultEntity.updatedOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="automatedPerformanceTestingApp.testResult.build">Build</Translate>
          </dt>
          <dd>{testResultEntity.build ? testResultEntity.build.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/test-result" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/test-result/${testResultEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TestResultDetail;
