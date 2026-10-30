
@file:Suppress(
  "KotlinRedundantDiagnosticSuppress",
  "PropertyName",
  "MayBeConstant",
  "RedundantVisibilityModifier",
  "RedundantCompanionReference",
  "RemoveEmptyClassBody",
  "SpellCheckingInspection",
  "unused",
)

package com.example.sportsyncapp.connector



public interface CreateInscripcionMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      DefaultConnector,
      CreateInscripcionMutation.Data,
      CreateInscripcionMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val eventoId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val equipoId: com.google.firebase.dataconnect.OptionalVariable<@kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID?>,
  
    val perfilId: com.google.firebase.dataconnect.OptionalVariable<@kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID?>,
  
    val fechaInscripcion: com.google.firebase.dataconnect.LocalDate,
  
    val estadoId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
  ) {
    
    
      
      @kotlin.DslMarker public annotation class BuilderDsl

      
      @BuilderDsl
      public interface Builder {
        public var eventoId: java.util.UUID
        public var equipoId: java.util.UUID?
        public var perfilId: java.util.UUID?
        public var fechaInscripcion: com.google.firebase.dataconnect.LocalDate
        public var estadoId: java.util.UUID
        
      }

      public companion object {
        
        @Suppress("NAME_SHADOWING")
        public fun build(
          eventoId: java.util.UUID,fechaInscripcion: com.google.firebase.dataconnect.LocalDate,estadoId: java.util.UUID,
          block_: Builder.() -> Unit
        ): Variables {
          var eventoId= eventoId
            var equipoId: com.google.firebase.dataconnect.OptionalVariable<java.util.UUID?> =
                com.google.firebase.dataconnect.OptionalVariable.Undefined
            var perfilId: com.google.firebase.dataconnect.OptionalVariable<java.util.UUID?> =
                com.google.firebase.dataconnect.OptionalVariable.Undefined
            var fechaInscripcion= fechaInscripcion
            var estadoId= estadoId
            

          return object : Builder {
            override var eventoId: java.util.UUID
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { eventoId = value_ }
              
            override var equipoId: java.util.UUID?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { equipoId = com.google.firebase.dataconnect.OptionalVariable.Value(value_) }
              
            override var perfilId: java.util.UUID?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { perfilId = com.google.firebase.dataconnect.OptionalVariable.Value(value_) }
              
            override var fechaInscripcion: com.google.firebase.dataconnect.LocalDate
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { fechaInscripcion = value_ }
              
            override var estadoId: java.util.UUID
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { estadoId = value_ }
              
            
          }.apply(block_)
          .let {
            Variables(
              eventoId=eventoId,equipoId=equipoId,perfilId=perfilId,fechaInscripcion=fechaInscripcion,estadoId=estadoId,
            )
          }
        }
      }
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val inscripciones_insert: InscripcionesKey,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "CreateInscripcion"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun CreateInscripcionMutation.ref(
  
    eventoId: java.util.UUID,fechaInscripcion: com.google.firebase.dataconnect.LocalDate,estadoId: java.util.UUID,

  
    block_: CreateInscripcionMutation.Variables.Builder.() -> Unit = {}
  
): com.google.firebase.dataconnect.MutationRef<
    CreateInscripcionMutation.Data,
    CreateInscripcionMutation.Variables
  > =
  ref(
    
      CreateInscripcionMutation.Variables.build(
        eventoId=eventoId,fechaInscripcion=fechaInscripcion,estadoId=estadoId,
  
    block_
      )
    
  )

public suspend fun CreateInscripcionMutation.execute(

  
    
      eventoId: java.util.UUID,fechaInscripcion: com.google.firebase.dataconnect.LocalDate,estadoId: java.util.UUID,

  
    block_: CreateInscripcionMutation.Variables.Builder.() -> Unit = {}

  ): com.google.firebase.dataconnect.MutationResult<
    CreateInscripcionMutation.Data,
    CreateInscripcionMutation.Variables
  > =
  ref(
    
      eventoId=eventoId,fechaInscripcion=fechaInscripcion,estadoId=estadoId,
  
    block_
    
  ).execute()


