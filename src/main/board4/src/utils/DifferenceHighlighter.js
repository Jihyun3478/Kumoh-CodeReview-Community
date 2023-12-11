import React from 'react';

function DifferenceHighlighter({ original, modified }) {
  const result = [];

  // 두 문자열 중 더 긴 길이를 찾습니다.
  const maxLength = Math.max(original.length, modified.length);

  for (let i = 0; i < maxLength; i++) {
    const originalChar = original[i];
    const modifiedChar = modified[i];

    // 문자열이 다를 경우 빨간색 스타일을 적용합니다.
    const isDifferent = originalChar !== modifiedChar;

    if (isDifferent) {
      result.push(
        <span key={i} style={{ color: 'red' }}>
          {modifiedChar}
        </span>
      );
    } else {
      result.push(
        <span key={i}>
          {originalChar}
        </span>
      );
    }
  }

  return <div>{result}</div>;
}

export default DifferenceHighlighter;