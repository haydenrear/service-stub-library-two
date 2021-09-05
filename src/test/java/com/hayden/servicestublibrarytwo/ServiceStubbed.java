package com.hayden.servicestublibrarytwo;


import org.springframework.stereotype.Service;

@ServiceStub(value = ServiceToStub.class, profile = "service")
@Service
public class ServiceStubbed extends ServiceToStub{
}
