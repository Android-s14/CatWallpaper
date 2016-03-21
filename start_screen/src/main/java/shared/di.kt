package shared

import javax.inject.Qualifier
import javax.inject.Scope

interface Component

@Scope
annotation class PerActivity

@Scope
annotation class PerApplication

