package com.taot.pms.gui

import com.twitter.finagle.Service
import com.twitter.finagle.thrift.{ThriftClientFramedCodec, ThriftClientRequest}
import com.twitter.finagle.builder.ClientBuilder
import java.net.InetSocketAddress
import org.apache.thrift.protocol.TBinaryProtocol
import com.taot.pms.rpc.thrift.PMSService

object ThriftClientFactory {

  def create() = {
    // Create a raw Thrift client service. This implements the
    // ThriftClientRequest => Future[Array[Byte]] interface.
    val service: Service[ThriftClientRequest, Array[Byte]] = ClientBuilder()
      .hosts(new InetSocketAddress(8080))
      .codec(ThriftClientFramedCodec())
      .hostConnectionLimit(1)
      .build()

    // Wrap the raw Thrift service in a Client decorator. The client provides
    // a convenient procedural interface for accessing the Thrift server.
    val client = new PMSService.FinagledClient(service, new TBinaryProtocol.Factory())

    client
  }
}
