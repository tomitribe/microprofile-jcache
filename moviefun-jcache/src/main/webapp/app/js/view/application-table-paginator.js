/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

(function () {
    'use strict';

    var deps = ['app/js/templates', 'lib/backbone'];
    define(deps, function (templates) {

        var View = Backbone.View.extend({
            tagName: 'ul',
            className: 'pagination',

            count: 0,
            page: 0,
            total: 0,

            events: {
                'click a': function (evt) {
                    evt.preventDefault();
                    var me = this;
                    var myLink = $(evt.target);
                    var href = myLink.attr('href');
                    me.trigger('go-to-page', {
                        number: href
                    });
                }
            },

            render: function () {
                var me = this;
                me.$el.empty();
                me.$el.append(templates.getValue('application-table-paginator-button', {
                    pageNumber: '1',
                    pageText: '<<'
                }));


                var low = 1;
                var high = me.count;

                if (me.count > 10) {
                    low = me.page > high - 9 ? high - 9 : me.page - 5;
                    if (low < 1) {
                        low = 1;
                    }

                    high = low + 9;
                }

                var i;
                for (i = low; i <= high; i += 1) {
                    me.$el.append(templates.getValue('application-table-paginator-button', {
                        pageNumber: i,
                        pageText: i
                    }));
                }

                me.$el.append(templates.getValue('application-table-paginator-button', {
                    pageNumber: 'last',
                    pageText: '>>'
                }));

                var from = ((me.page - 1) * 50) + 1;
                var to = me.total - from < 49 ? me.total : from + 49;

                me.$el.append(templates.getValue('application-table-paginator-count', {
                    from: from,
                    to: to,
                    count: me.total
                }));
                return this;
            },

            setCount: function (count) {
                var me = this;
                me.count = count;
                me.render();
            },

            getCount: function () {
                var me = this;
                return me.count;
            },

            setPage: function (page) {
                var me = this;
                me.page = page;
                me.render();
            },

            getPage: function () {
                var me = this;
                return me.page;
            },

            setTotal: function (total) {
                var me = this;
                me.total = total;
                me.render();
            },

            getTotal: function () {
                var me = this;
                return me.total;
            }
        });
        return new View().render();
    });
}());
