import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IBuild } from 'app/shared/model/build.model';
import { getEntities as getBuilds } from 'app/entities/build/build.reducer';
import { ITestResult } from 'app/shared/model/test-result.model';
import { getEntity, updateEntity, createEntity, reset } from './test-result.reducer';

export const TestResultUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const builds = useAppSelector(state => state.build.entities);
  const testResultEntity = useAppSelector(state => state.testResult.entity);
  const loading = useAppSelector(state => state.testResult.loading);
  const updating = useAppSelector(state => state.testResult.updating);
  const updateSuccess = useAppSelector(state => state.testResult.updateSuccess);

  const handleClose = () => {
    navigate('/test-result');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getBuilds({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...testResultEntity,
      ...values,
      build: builds.find(it => it.id.toString() === values.build.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...testResultEntity,
          build: testResultEntity?.build?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="automatedPerformanceTestingApp.testResult.home.createOrEditLabel" data-cy="TestResultCreateUpdateHeading">
            <Translate contentKey="automatedPerformanceTestingApp.testResult.home.createOrEditLabel">Create or edit a TestResult</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="test-result-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('automatedPerformanceTestingApp.testResult.pageLoad')}
                id="test-result-pageLoad"
                name="pageLoad"
                data-cy="pageLoad"
                type="text"
              />
              <ValidatedField
                label={translate('automatedPerformanceTestingApp.testResult.timeToInteractive')}
                id="test-result-timeToInteractive"
                name="timeToInteractive"
                data-cy="timeToInteractive"
                type="text"
              />
              <ValidatedField
                label={translate('automatedPerformanceTestingApp.testResult.createdBy')}
                id="test-result-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('automatedPerformanceTestingApp.testResult.createdOn')}
                id="test-result-createdOn"
                name="createdOn"
                data-cy="createdOn"
                type="date"
              />
              <ValidatedField
                label={translate('automatedPerformanceTestingApp.testResult.updatedBy')}
                id="test-result-updatedBy"
                name="updatedBy"
                data-cy="updatedBy"
                type="text"
              />
              <ValidatedField
                label={translate('automatedPerformanceTestingApp.testResult.updatedOn')}
                id="test-result-updatedOn"
                name="updatedOn"
                data-cy="updatedOn"
                type="date"
              />
              <ValidatedField
                id="test-result-build"
                name="build"
                data-cy="build"
                label={translate('automatedPerformanceTestingApp.testResult.build')}
                type="select"
              >
                <option value="" key="0" />
                {builds
                  ? builds.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/test-result" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default TestResultUpdate;
