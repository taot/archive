package com.github.taot.sbt.i18n

import scala.util.parsing.combinator._
import com.github.taot.sbt.i18n.model._

trait ResourceParser extends RegexParsers {

  override def skipWhitespace = false

  def ident = regex("[_a-zA-Z][_a-zA-Z0-9]*".r)

  def typeIdent = regex("[_a-zA-Z][\\.\\[\\]_a-zA-Z0-9]*".r)

  /* Parsers for key (left side of a resource) */
  def keyArg = "{" ~ ident ~ regex("\\s*:\\s*".r) ~ ident ~ "}" ^^ {
    case _ ~ value ~ _ ~ tpe ~ _ => KeyNodeArg(value, tpe)
  }

  def keyComp: Parser[KeyNodeValue] = (keyArg | ident) ^^ {
    case arg: KeyNodeArg => Right(arg)
    case ident: String => Left(ident)
  }

  def key: Parser[List[KeyNodeValue]] = keyComp ~ rep("." ~> keyComp) ^^ {
    case (k: KeyNodeValue) ~ (rest: List[KeyNodeValue]) => k :: rest
  }

  /* Parsers for message func (right side of a resource */
  def msgArg: Parser[MessageComponent] = "{" ~ opt(ident <~ regex("\\s*:\\s*".r)) ~ typeIdent ~ "}" ^^ {
    case _ ~ nameOpt ~ tpe ~ _ => Right(MessageFuncArg(nameOpt, tpe))
  }

  def msgLiteral: Parser[MessageComponent] = regex("[^{}]*".r) ^^ {s => Left(s) }

  def msgFunc1 = msgLiteral ~ rep(msgArg ~ msgLiteral) ~ opt(msgArg) ^^ {
    case lit ~ list ~ argOpt => {
      val mid = list.flatMap { tuple =>
        tuple match {
          case a ~ l => Seq(a, l)
        }
      }
      MessageFunc(lit :: mid ++ argOpt.toList)
    }
  }

  def msgFunc2 = msgArg ~ rep(msgLiteral ~ msgArg) ~ opt(msgLiteral) ^^ {
    case arg ~ list ~ litOpt => {
      val mid = list.flatMap { tuple =>
        tuple match {
          case l ~ a => Seq(l, a)
        }
      }
      MessageFunc(arg :: mid ++ litOpt.toList)
    }
  }

  def msgFunc = (msgFunc1 | msgFunc2) ^^ { list => list}

  /* The whole thing */
  def resource: Parser[(List[KeyNodeValue], MessageFunc)] = key ~ regex("\\s*=\\s*".r) ~ msgFunc ^^ {
    case k ~ _ ~ mf => (k, mf)
  }
}
