import React from 'react'
import WriteBoard from '../components/WriteBoard'
import Title from '../components/Title'

function WritePage({additionalProp}) {
  return (
    <>
    <Title pageId={additionalProp}/>
    <WriteBoard />
    </>
  )
}

export default WritePage