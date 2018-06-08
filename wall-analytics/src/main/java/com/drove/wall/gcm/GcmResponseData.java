package com.drove.wall.gcm;

import com.google.gson.annotations.Expose;

/**
 * Created by arjun on 11/11/15.
 */
public class GcmResponseData {

        @Expose
        public String rid;

        @Expose
        public Sender sender;

        public class Sender {
            @Expose
            public String _id;

            @Expose
            public String username;

        }
        @Expose
        public String type;

        @Expose
        public String name;
}
