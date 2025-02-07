package com.Giga_JAD.Wapi_Wapi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder // Works properly now
public class StripeResponse {
    private String status;
    private String message;
    private String sessionId;
    private String sessionUrl;

//    private StripeResponse(String status, String message, String sessionId, String sessionUrl) {
//        this.status = status;
//        this.message = message;
//        this.sessionId = sessionId;
//        this.sessionUrl = sessionUrl;
//    }
//
//    public static StripeResponseBuilder builder() {
//        return new StripeResponseBuilder();
//    }
//
//    public static class StripeResponseBuilder {
//        private String status;
//        private String message;
//        private String sessionId;
//        private String sessionUrl;
//
//        public StripeResponseBuilder status(String status) {
//            this.status = status;
//            return this;
//        }
//
//        public StripeResponseBuilder message(String message) {
//            this.message = message;
//            return this;
//        }
//
//        public StripeResponseBuilder sessionId(String sessionId) {
//            this.sessionId = sessionId;
//            return this;
//        }
//
//        public StripeResponseBuilder sessionUrl(String sessionUrl) {
//            this.sessionUrl = sessionUrl;
//            return this;
//        }
//
//        public StripeResponse build() {
//            return new StripeResponse(status, message, sessionId, sessionUrl);
//        }
//    }
}

