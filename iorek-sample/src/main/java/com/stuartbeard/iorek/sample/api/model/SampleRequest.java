/*
 * MIT License
 *
 * Copyright (c) 2019-2021 Stuart Beard
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.stuartbeard.iorek.sample.api.model;

import com.stuartbeard.iorek.constraints.NotKnowinglyCompromised;
import com.stuartbeard.iorek.constraints.PasswordDoesNotContainUsername;
import lombok.Data;

@Data
@PasswordDoesNotContainUsername(message = "The password provided contains the username and cannot be used.")
public class SampleRequest {

    private String username;

    @NotKnowinglyCompromised(message = "The password provided for {requestFlow} was too severely compromised and cannot be used.", requestFlow = "in-band-sample")
    private String password;
}
