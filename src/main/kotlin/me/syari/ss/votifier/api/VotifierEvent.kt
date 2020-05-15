package me.syari.ss.votifier.api

import me.syari.ss.core.event.CustomEvent

class VotifierEvent(val vote: Vote): CustomEvent()