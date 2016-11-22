# -*- coding: utf-8 -*-

import json
import requests
import time


XSHE_160717 = '160717.XSHE'
HSCEI = 'HSCEI'

HKDCNYC = 0.07

PCLOSE_PRICES = {
    XSHE_160717: 0.660,
    HSCEI: 9427.93
}

XSHE_160717_P_NET_VALUE = 0.6666

SLEEP_INTERVAL = 5


def calc_estimated_net_value(lof_data, hscei_data):
    nv = XSHE_160717_P_NET_VALUE * hscei_data.close_price / hscei_data.prev_close_price
    nv *= (1 + HKDCNYC / 100.0)
    return nv


def calc_estimated_return(prev_net_value, net_value, lof_data):
    pnl = net_value * (1 - 0.005) - lof_data.close_price
    ret = pnl / lof_data.close_price
    return ret


class MarketData:
    def __init__(self, ticker, close_price, prev_close_price, timestamp):
        self.ticker = ticker
        self.close_price = close_price * 1.0
        self.prev_close_price = prev_close_price * 1.0
        self.timestamp = timestamp

    def get_return(self):
        return self.close_price / self.prev_close_price - 1.0


def get_marketdata(tickers):
    r = requests.get('http://vip.newapi.wmcloud-stg.com/v1/api/market/getTickRTSnapshot.json?securityID=%s' % ','.join(tickers))
    data = json.loads(r.text)
    symbol_map = {}
    marketdata = {}
    for t in tickers:
        s = t.split('.')[0]
        symbol_map[s] = t
    for obj in data['data']:
        sym = obj['ticker']
        ticker = symbol_map[sym]
        last_price = obj['lastPrice']
        prev_close_price = PCLOSE_PRICES[ticker]
        timestamp = obj['timestamp']
        md = MarketData(ticker=ticker, close_price=last_price, prev_close_price=prev_close_price, timestamp=timestamp)
        marketdata[ticker] = md
    return marketdata


def format_timetamp(ts):
    return time.asctime(time.localtime(ts / 1000.0))


def run_calc():
    curtime = time.asctime()
    print('[%s]' % curtime)
    tickers = [XSHE_160717, HSCEI]
    marketdata = get_marketdata(tickers)

    header_format_str = '%12s%18s%18s%18s%18s%18s%18s%30s'
    data_format_str = '%12s%18.3f%18.3f%18.3f%18.3f%18.3f%18.3f%30s'

    print(header_format_str % ('TICKER', 'P_CLOSE_PRICE', 'CLOSE_PRICE', 'PRICE %',
                               'P_NET_VALUE', 'NET_VALUE (EST)', 'NET_VALUE %', 'TIMESTAMP'))

    lof_md = marketdata[XSHE_160717]
    hscei_md = marketdata[HSCEI]

    net_value_est = calc_estimated_net_value(lof_md, hscei_md)
    return_est = calc_estimated_return(XSHE_160717_P_NET_VALUE, net_value_est, lof_md)
    print(data_format_str % (lof_md.ticker, lof_md.prev_close_price, lof_md.close_price, lof_md.get_return() * 100.0,
                             XSHE_160717_P_NET_VALUE, net_value_est, (net_value_est / XSHE_160717_P_NET_VALUE - 1) * 100.0,
                             format_timetamp(lof_md.timestamp)))
    print(data_format_str % (hscei_md.ticker, hscei_md.prev_close_price, hscei_md.close_price, hscei_md.get_return() * 100.0,
                             0, 0, 0, format_timetamp(hscei_md.timestamp)))
    print('')
    yield_est = return_est * 100.0
    print('Estimated Yield %%: %s' % yield_est)
    print('')


def main():
    print('')
    while True:
        run_calc()
        time.sleep(SLEEP_INTERVAL)

if __name__ == '__main__':
    # get_marketdata([XSHE_160717, HSCEI])
    main()
