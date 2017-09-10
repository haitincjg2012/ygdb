package com.apec.framework.common.enumtype;

public enum RoutingKey
{
    TEST( "TEST" ), TEST_FOO( "TEST_FOO" ), TEST_BAR( "TEST_BAR" ), ORDE_TO_LGS( "ORDE_TO_LGS" ), LGS_SEND_TO_SORTING(
    "LGS_SEND_TO_SORTING" ), LOGISTICS_TO_PAY( "LOGISTICS_TO_PAY" ), LGS_SEND_TO_ORDER_2(
    "LGS_SEND_TO_ORDER_2" ), SORTING_TO_LGS( "SORTING_TO_LGS" ), PAY_TO_LGS_STATUS(
    "PAY_TO_LGS_STATUS" ), PAY_TO_LGS_THIRDNO( "PAY_TO_LGS_THIRDNO" ),SORTING_TO_LGS2( "SORTING_TO_LGS2" )
    ,COUPON_OWN_USERS("COUPON_OWN_USERS"),
    PAY_SEND_TO_ORDER("PAY_SEND_TO_ORDER"),
    WEB_TO_LOG( "WEB_TO_LOG" ), GOODS_TO_SHOPCART( "GOODS_TO_SHOPCART" );

    RoutingKey(String name)
    {
        this.name = name;
    }

    private final String name;

    public final String getName()
    {
        return name;
    }
}
