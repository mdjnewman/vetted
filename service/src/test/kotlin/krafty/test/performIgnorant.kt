package krafty.test

import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch

fun MockMvc.performIgnorant(with: MockHttpServletRequestBuilder): ResultActions {

    val resultActions = this.perform(with)

    if (resultActions.andReturn().request.isAsyncStarted) {
        return this.perform(asyncDispatch(resultActions.andReturn()))
    } else {
        return resultActions
    }

}