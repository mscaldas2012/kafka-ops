import React from 'react';
import styled from 'styled-components';

const StyledSpinner = styled.div`
  border: 4px solid #f3f3f3;
  border-top: 4px solid #105eab;
  border-bottom: 4px solid #4ebaaa;

  border-radius: 50%;
  width: 100px;
  height: 100px;
  animation: spin 2s linear infinite;
  margin: auto;

  @keyframes spin {
    0% {
      transform: rotate(0deg);
    }
    100% {
      transform: rotate(360deg);
    }
  }
`;

const Spinner = () => {
    return (
        <div className="col-12">
            <StyledSpinner />
        </div>
    );
}; // ./Spinner

export default Spinner;