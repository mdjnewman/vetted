package me.mdjnewman.vetted.client

import me.mdjnewman.vetted.model.ClientResource
import org.springframework.cloud.netflix.feign.FeignClient

@FeignClient(ClientResource.NAME, path = ClientResource.PATH)
interface VettedClientResource : ClientResource
