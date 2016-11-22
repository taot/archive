package com.taot.pms.domain.model.asset

import com.taot.pms.persist.{ Position => DbPosition, PositionHist => DbPositionHist }

trait RecordConverter {

  def toRecords(position: Position): List[DbPositionHist]

  def fromRecords(positionRec: DbPosition, records: List[DbPositionHist]): Position
}
